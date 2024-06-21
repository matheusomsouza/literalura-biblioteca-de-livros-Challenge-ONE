package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    private LivroRepository repositorio;
    private AutorRepository repositorioAutor;
    private Optional<Livro> livroBusca;
    private Optional<Autor> autorBusca;

    public Principal(LivroRepository repositorio, AutorRepository repositorioAutor) {
        this.repositorio = repositorio;
        this.repositorioAutor = repositorioAutor;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    ***** LITERALURA *****
                    
                    ESCOLHA UM NÚMERO DE SUA OPÇÃO:
                    
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - listar autores ativos em uma determinada data
                    5 - Listar livros em um determinado idioma
                    6 - Top 3 livro mais baixados
                                    
                    0 - Sair
                    
                    ***********************                      
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroApi();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresPorData();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    listarTop3Livros();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private DadosResults getDadosLivro() {
        System.out.println("Digite o nome da livro para busca: ");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.toLowerCase().replace(" ", "%20"));
        DadosResults dados = conversor.obterDados(json, DadosResults.class);
        return dados;
    }

    private void buscarLivroApi() {
        DadosResults dados = getDadosLivro();
        Livro livro = new Livro(dados);
        Autor autor = new Autor(dados.results().get(0).autor().get(0));
        autorBusca = repositorioAutor.findByNomeContaining(autor.getNome());
        if(autorBusca.isPresent()) {
            Autor autorBuscado = autorBusca.get();
            autorBuscado.setLivros(livro);
            repositorioAutor.save(autorBuscado);
        } else {
            autor.setLivros(livro);
            repositorioAutor.save(autor);
        }

        livroBusca = repositorio.findByTitulo(livro.getTitulo());
        if(livroBusca.isPresent()) {
            System.out.println("Livro já cadastrado no banco de dados!");
        } else {
            repositorio.save(livro);
        }
        System.out.println(livro);
    }

    private void listarLivros() {
        System.out.println("LIVROS DO BANCO DE DADOS:");
        List<Livro> livros;
        livros = repositorio.findAll();
        livros.forEach(System.out::println);
    }

    private void listarAutores() {
        System.out.println("AUTORES DO BANCO DE DADOS:");
        List<Autor> autores;
        autores = repositorioAutor.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresPorData() {
        System.out.println("Digite o ano que gostaria de filtrar os autores ativos: ");
        var dataBusca = leitura.nextInt();
        leitura.nextLine();
        System.out.println("AUTORES ATIVOS NA DATA:");
        List<Autor> autoresAtivos;
        autoresAtivos = repositorioAutor.findByDataNLessThanEqualAndDataFGreaterThanEqual(dataBusca, dataBusca);
        autoresAtivos.forEach(System.out::println);
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
                Digite o idioma que gostaria de filtrar os livros:
                pt-Português
                en-Inglês
                es-Espanhol
                fr-Francês
                """);
        var idiomaBusca = leitura.nextLine();
        System.out.println("LIVROS NO IDIOMA ESCOLHIDO:");
        List<Livro> livrosIdioma;
        livrosIdioma = repositorio.findByIdioma(idiomaBusca);
        livrosIdioma.forEach(System.out::println);
    }

    private void listarTop3Livros() {
        System.out.println("TOP 3 LIVROS DO BANCO DE DADOS MAIS BAIXADOS:");
        List<Livro> listaTop3;
        listaTop3 = repositorio.findTop3ByOrderByNumeroDownloadsDesc();
        listaTop3.forEach(System.out::println);
    }
}
