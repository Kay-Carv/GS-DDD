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

        // 1. Garantir que os Armazéns existem (sem apagar os existentes)
        Armazem hubTerrestre = armazemRepository.findAll().stream()
                .filter(a -> a.getNome().equals("Hub Logístico Terrestre - Alcântara"))
                .findFirst()
                .orElseGet(() -> armazemRepository.save(new Armazem("Hub Logístico Terrestre - Alcântara", "Maranhão, Brasil", false)));

        Armazem baseLunar = armazemRepository.findAll().stream()
                .filter(a -> a.getNome().equals("Complexo Habitacional Artemis-III"))
                .findFirst()
                .orElseGet(() -> armazemRepository.save(new Armazem("Complexo Habitacional Artemis-III", "Cratera Shackleton", true)));

        // 2. Garantir que os Produtos existem (sem apagar o seu produto de teste)
        // Usamos o count() ou uma busca pelo nome para saber se já inserimos
        if (produtoRepository.findAll().stream().noneMatch(p -> p.getNome().equals("Cilindros de Oxigênio (O2)"))) {

            System.out.println("🌱 [StarAge] Inserindo novos produtos...");

            Produto oxigenio = new Produto("Cilindros de Oxigênio (O2)", baseLunar, 45, 500, LocalDate.now().plusMonths(18));
            Produto baterias = new Produto("Baterias de Íon-Lítio", hubTerrestre, 1450, 2000, LocalDate.now().plusYears(4));

            produtoRepository.saveAll(Arrays.asList(oxigenio, baterias));

            System.out.println("✅ Novos produtos inseridos!");
        } else {
            System.out.println("ℹ️ Produtos já existem, pulando inserção.");
        }
    }
}