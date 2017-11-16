package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resultado {

    protected Integer idFactura;
    protected String mensaje;

    public Resultado() {
    }

    public Resultado(Integer idFactura, String mensaje) {
        this.idFactura = idFactura;
        this.mensaje = mensaje;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
