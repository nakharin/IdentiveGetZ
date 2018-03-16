package identive.usb;

/**
 * Created by nakharin on 16/3/2018 AD.
 */

public class Personal {

    private String personal;
    private String address;
    private String issueExpire;
    private String citizenId;
    private String[] thPersonal;
    private String[] enPersonal;
    private String photoJpeg;

    public String getCitizenId() {
        return citizenId.replaceAll("[^\\d]", "");
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getPhotoRaw() {
        return photoJpeg;
    }

    public void setPhotoRaw(String value) {
        photoJpeg = value;
    }

    public void setPersonalInfo(String value) {
        personal = value;
        thPersonal = personal.substring(0, 100).split("#");
        enPersonal = personal.substring(100, 200).split("#");
        System.out.println("PersonalInfoTH :" + personal.substring(0, 100));
        System.out.println("PersonalInfoEN :" + personal.substring(100, 200));
    }

    public String getBirthday() {
        int birthYear = Integer.valueOf(personal.substring(200, 204)) - 543;
        String birthMonth = personal.substring(204, 206);
        String birthDay = personal.substring(206, 208);

        return birthDay + "-" + birthMonth + "-" + (String.valueOf(birthYear));
    }

    public String getSex() {
        return personal.substring(208, 209);
    }

    public String getThPrefix() {
        return thPersonal[0].trim();
    }

    public String getTHFirstName() {
        return thPersonal[1].trim();
    }

    public String getTHLastName() {
        return thPersonal[3].trim();
    }

    public String getENPrefix() {
        return enPersonal[0].trim();
    }

    public String getENFirstName() {
        return enPersonal[1].trim();
    }

    public String getENLastName() {
        return enPersonal[3].trim();
    }

    public String getType() {
        return personal.split("#")[0].trim();
    }

    public String getAddress() {
        return address.replace("#", " ").substring(0, address.length() - 3);
    }

    public void setAddress(String value) {
        address = value.trim();
    }

    public String getHouseNo() {
        return address.split("#")[0].trim();
    }

    public String getVillageNo() {
        return address.split("#")[1].trim();
    }

    public String getLane() {
        return address.split("#")[2].trim();
    }

    public String getRoad() {
        return address.split("#")[3].trim();
    }

    public String getTambol() {
        return address.split("#")[5].trim();
    }

    public String getAmphur() {
        return address.split("#")[6].trim();
    }

    public String getProvince() {
        String ProvinceName = address.split("#")[7].trim().replace("จังหวัด", "").replace(" ", "");
        return ProvinceName.substring(0, ProvinceName.length() - 1);
    }

    public void setIssueExpire(String value) {
        issueExpire = value.trim();
    }

    public String getDateTimeIssue() {
        int issueYear = Integer.valueOf(issueExpire.substring(0, 4)) - 543;
        String issueMonth = issueExpire.substring(4, 6);
        String issueDay = issueExpire.substring(6, 8);
        return issueDay + "-" + issueMonth + "-" + (String.valueOf(issueYear));
    }

    public String getDateExpire() {
        int expireYear = Integer.valueOf(issueExpire.substring(8, 12)) - 543;
        String expireMonth = issueExpire.substring(12, 14);
        String expireDay = issueExpire.substring(14, 16);

        return expireDay + "-" + expireMonth + "-" + (String.valueOf(expireYear));
    }
}
