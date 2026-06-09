package br.com.fiap.starage.infrastructure.config;

import br.com.fiap.starage.domain.model.Armazem;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.ArmazemRepository;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    private final ArmazemRepository armazemRepository;
    private final ProdutoRepository produtoRepository;

    public DatabaseSeeder(ArmazemRepository armazemRepository, ProdutoRepository produtoRepository) {
        this.armazemRepository = armazemRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (armazemRepository.count() == 0) {
            System.out.println("🌱 [StarAge Eco-System] Populando banco de dados com recursos de uso duplo (Terra-Lua)...");

            // 1. Instanciando os Hubs logísticos estratégicos
            Armazem hubTerrestre = new Armazem("Hub Logístico Terrestre - Alcântara", "Maranhão, Brasil - Setor de Carga", true);
            Armazem baseLunar = new Armazem("Complexo Habitacional Artemis-III", "Domo de Colonização Primária - Cratera Shackleton", true);
            armazemRepository.saveAll(Arrays.asList(hubTerrestre, baseLunar));

            // 2. Recursos de Suporte à Vida e Manutenção Tecnológica

            // Item Crítico (Estoque baixo e alta queima no Domo Lunar)
            Produto oxigenio = new Produto(
                    "Cilindros de Oxigênio Comprimido (O2)",
                    baseLunar,
                    45,   // Quantidade baixa atual
                    500,  // Capacidade total do compartimento
                    LocalDate.now().plusMonths(18)
            );

            // Item em Estado de Alerta (Filtros de recirculação essenciais)
            Produto filtrosAgua = new Produto(
                    "Membranas de Filtragem por Osmose Reversa",
                    baseLunar,
                    110,
                    600,
                    LocalDate.now().plusMonths(12)
            );

            // Item Seguro (Grande volume armazenado no Hub Terrestre pronto para envio)
            Produto baterias = new Produto(
                    "Células de Bateria de Íon-Lítio de Estado Sólido",
                    hubTerrestre,
                    1450,
                    2000,
                    LocalDate.now().plusYears(4)
            );

            // Selos térmicos para radiação e vácuo
            Produto isolantes = new Produto(
                    "Selos de Isolamento Térmico de Grafeno",
                    hubTerrestre,
                    320,
                    1000,
                    LocalDate.now().plusYears(2)
            );

            produtoRepository.saveAll(Arrays.asList(oxigenio, filtrosAgua, baterias, isolantes));

            System.out.println("✅ Base de dados reestruturada com foco em logística terrestre-espacial!");
        }
    }
}