package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")

public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autorNome;
    private String idioma;
    private String numeroDownloads;
    @ManyToOne
    private Autor autor;

    public Livro() {
    }

    public Livro(DadosResults dadosResults) {
        this.titulo = dadosResults.results().get(0).titulo();
        this.autorNome = dadosResults.results().get(0).autor().get(0).nome();
        this.idioma = dadosResults.results().get(0).idioma().get(0);
        this.numeroDownloads = dadosResults.results().get(0).numeroDownloads();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(String numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public String nomeAutor(DadosLivro dadosLivro) {
        return dadosLivro.autor().get(0).nome();
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\nTitulo: " + titulo +
                "\nAutor: " + autorNome +
                "\nIdioma: " + idioma +
                "\nNumero de Downloads: " + numeroDownloads +
                "\n";
    }
}
