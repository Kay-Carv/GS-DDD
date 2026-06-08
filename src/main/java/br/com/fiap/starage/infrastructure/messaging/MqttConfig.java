package br.com.fiap.starage.infrastructure.messaging;

import br.com.fiap.starage.application.service.ProcessadorSensoresService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Autowired
    private ProcessadorSensoresService processadorSensoresService;

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "starage_backend_receiver";
    private static final String TOPIC_SENSORES = "starage/armazens/1/sensores";

    // 1️⃣ Configuração explícita das opções de conexão do cliente Paho
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { BROKER_URL });
        options.setCleanSession(true);          // Força o broker a limpar sessões antigas travadas
        options.setAutomaticReconnect(true);    // Se cair, reconecta sozinho
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        // Usamos a Factory para construir o adaptador de forma estável
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID, mqttClientFactory(), TOPIC_SENSORES);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    // 2️⃣ Handler principal (Fluxo Feliz)
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                String payload = message.getPayload().toString();
                System.out.println("\n📬 [SINAL DETECTADO] -> Chegou com sucesso no Handler: " + payload);
                processadorSensoresService.processarMensagemMqtt(payload);
            } catch (Exception e) {
                System.err.println("❌ Erro ao processar payload no Handler: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    // 3️⃣ 🚨 O CAÇADOR DE ERROS OCULTOS 🚨
    // Se a mensagem chegar do Docker mas falhar em qualquer validação interna do Spring, cai aqui!
    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errorHandler() {
        return message -> {
            System.err.println("\n🚨 [ERROR CHANNEL] O Spring Integration interceptou uma falha no fluxo MQTT!");
            System.err.println("Causa raiz do problema: " + message.getPayload());
            if (message.getPayload() instanceof Throwable) {
                ((Throwable) message.getPayload()).printStackTrace();
            }
        };
    }
}