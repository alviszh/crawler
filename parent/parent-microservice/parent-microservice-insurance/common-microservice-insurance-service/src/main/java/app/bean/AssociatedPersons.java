/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2018-03-02 15:40:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AssociatedPersons {

    @Override
	public String toString() {
		return "AssociatedPersons [id=" + id + ", personNumber=" + personNumber + ", name=" + name + ", insuranceType="
				+ insuranceType + ", roleId=" + roleId + "]";
	}
	private long id;
    private String personNumber;
    private String name;
    private String insuranceType;
    private String roleId;
    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
         return id;
     }

    public void setPersonNumber(String personNumber) {
         this.personNumber = personNumber;
     }
     public String getPersonNumber() {
         return personNumber;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setInsuranceType(String insuranceType) {
         this.insuranceType = insuranceType;
     }
     public String getInsuranceType() {
         return insuranceType;
     }

    public void setRoleId(String roleId) {
         this.roleId = roleId;
     }
     public String getRoleId() {
         return roleId;
     }

}