package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")

@JsonIgnoreProperties(ignoreUnknown = true)

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    private Integer dataN;
    private Integer dataF;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DadosAutor dadosAutor) {
        this.nome = dadosAutor.nome();
        this.dataN = dadosAutor.dataN();
        this.dataF = dadosAutor.dataF();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDataN() {
        return dataN;
    }

    public void setDataN(Integer dataN) {
        this.dataN = dataN;
    }

    public Integer getDataF() {
        return dataF;
    }

    public void setDataF(Integer dataF) {
        this.dataF = dataF;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Livro livro) {
        this.livros.add(livro);
        livro.setAutor(this);
    }

    @Override
    public String toString() {
        return "\nNome: " + nome +
                "\nDataN: " + dataN +
                "\nDataF: " + dataF +
                "\nLivros: " + livros.stream().map(l -> l.getTitulo()).collect(Collectors.toList());
    }
}