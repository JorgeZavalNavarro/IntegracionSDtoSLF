
package com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf;

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
 *         &lt;element name="Result" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "result",
    "resultID",
    "resultDescription"
})
@XmlRootElement(name = "processResponse")
public class ProcessResponse {

    @XmlElement(name = "Result", required = true)
    protected String result;
    @XmlElement(name = "ResultID", required = true)
    protected String resultID;
    @XmlElement(name = "ResultDescription", required = true)
    protected String resultDescription;

    /**
     * Obtiene el valor de la propiedad result.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResult() {
        return result;
    }

    /**
     * Define el valor de la propiedad result.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Obtiene el valor de la propiedad resultID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultID() {
        return resultID;
    }

    /**
     * Define el valor de la propiedad resultID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultID(String value) {
        this.resultID = value;
    }

    /**
     * Obtiene el valor de la propiedad resultDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultDescription() {
        return resultDescription;
    }

    /**
     * Define el valor de la propiedad resultDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultDescription(String value) {
        this.resultDescription = value;
    }

}
