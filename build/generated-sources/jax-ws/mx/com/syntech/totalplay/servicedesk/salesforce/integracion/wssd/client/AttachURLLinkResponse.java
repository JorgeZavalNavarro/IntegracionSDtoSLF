
package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attachURLLinkReturn" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "attachURLLinkReturn"
})
@XmlRootElement(name = "attachURLLinkResponse")
public class AttachURLLinkResponse {

    protected int attachURLLinkReturn;

    /**
     * Obtiene el valor de la propiedad attachURLLinkReturn.
     * 
     */
    public int getAttachURLLinkReturn() {
        return attachURLLinkReturn;
    }

    /**
     * Define el valor de la propiedad attachURLLinkReturn.
     * 
     */
    public void setAttachURLLinkReturn(int value) {
        this.attachURLLinkReturn = value;
    }

}
