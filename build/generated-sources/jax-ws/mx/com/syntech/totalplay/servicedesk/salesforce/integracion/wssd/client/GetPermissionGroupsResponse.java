
package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="getPermissionGroupsReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "getPermissionGroupsReturn"
})
@XmlRootElement(name = "getPermissionGroupsResponse")
public class GetPermissionGroupsResponse {

    @XmlElement(required = true)
    protected String getPermissionGroupsReturn;

    /**
     * Obtiene el valor de la propiedad getPermissionGroupsReturn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetPermissionGroupsReturn() {
        return getPermissionGroupsReturn;
    }

    /**
     * Define el valor de la propiedad getPermissionGroupsReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetPermissionGroupsReturn(String value) {
        this.getPermissionGroupsReturn = value;
    }

}
