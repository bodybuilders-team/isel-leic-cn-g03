package cn.operations;

import java.util.Date;
import java.util.Map;

public class Evento {
    public int evtID;
    public String nome;
    public String tipo;
    public Date dtInicio;
    public Date dtFinal;
    public Licenciamento licenciamento;
    public Map<String, String> details;

    public Evento() {}
}
