
package com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BPELProcessChangeStatusTicketSF", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BPELProcessChangeStatusTicketSF {


    /**
     * 
     * @param result
     * @param resultID
     * @param comment
     * @param resultDescription
     * @param login
     * @param subStatus
     * @param noTicket
     * @param status
     */
    @WebMethod(action = "process")
    @RequestWrapper(localName = "process", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF", className = "com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.Process")
    @ResponseWrapper(localName = "processResponse", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF", className = "com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.ProcessResponse")
    public void process(
        @WebParam(name = "Login", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
        com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.Process.Login login,
        @WebParam(name = "NoTicket", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
        String noTicket,
        @WebParam(name = "Status", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
        String status,
        @WebParam(name = "SubStatus", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
        String subStatus,
        @WebParam(name = "Comment", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF")
        String comment,
        @WebParam(name = "Result", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF", mode = WebParam.Mode.OUT)
        Holder<String> result,
        @WebParam(name = "ResultID", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF", mode = WebParam.Mode.OUT)
        Holder<String> resultID,
        @WebParam(name = "ResultDescription", targetNamespace = "http://soa.totalplay.com/SalesForce/ChangeStatusTicketSF/BPELProcessChangeStatusTicketSF", mode = WebParam.Mode.OUT)
        Holder<String> resultDescription);

}