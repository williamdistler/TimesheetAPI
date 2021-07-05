package com.mediaportal.timesheetapi;

class Valores {

    private String funcionario;
    private String data;
    private String horasTrabalhadas;
    private String idProjeto;
    private String projeto;
    
    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(String horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public String getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(String idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    @Override
    public String toString() {
        return "Nome: " + getFuncionario() + " projeto " + getProjeto() + " data " + getData() + " horas " + getHorasTrabalhadas(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
