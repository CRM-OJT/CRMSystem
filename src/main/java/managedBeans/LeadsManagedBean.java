package managedBeans;

import ejb.LeadsFacade;
import ejb.UsersFacade;
import entities.Leads;
import entities.Users;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Named(value = "leadsManagedBean")
@RequestScoped
public class LeadsManagedBean implements Serializable {

    @Inject
    private LeadsFacade leadEJB;   
    @Inject
    private UsersFacade userEJB;
    
    private HttpSession session = (HttpSession) FacesContext.
                    getCurrentInstance().getExternalContext().getSession(false);   
    private Leads lead = new Leads();
    private List<Leads> searchList;   
    private Users user = (Users)session.getAttribute("userlogin");
    private Integer id;
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
       
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Leads> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Leads> searchList) {
        this.searchList = searchList;
    }

    public Leads getLead() {
        return lead;
    }

    public void setLead(Leads lead) {
        this.lead = lead;
    }

    public LeadsManagedBean() {
    }
    
    public String doCreateLead(){
        lead.setUserID(user);
        if(leadEJB.isExistLeads(lead)){            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Lead Email is Existed",
                            "Lead Email is Existed"));
        } else {
            leadEJB.create(lead);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Create Success",
                            "Create Success !"));
            lead = new Leads();
        }
        return "addlead.xhtml";
    }

    public List<Leads> doFindAllLeads() {
        return leadEJB.doFindAllLeadsByUser(user.getUserID());
    }

    public String doEditLead() {
        lead = leadEJB.find(id);
        return "editlead.xhtml";
    }
    
    public String applyEditLead(){
        lead.setUserID(user);
        leadEJB.edit(lead);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Success", "Edit Success !"));
        return "editlead.xhtml";
    }

    public String doDeleteLead(Integer id) {
        leadEJB.remove(leadEJB.find(id));
        return "listleads.xhtml";
    }
     
}
