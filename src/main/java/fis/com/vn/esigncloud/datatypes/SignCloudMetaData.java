package fis.com.vn.esigncloud.datatypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignCloudMetaData {
    private HashMap<String, String> singletonSigning;
    private HashMap<String, String> counterSigning;

    @JsonProperty("singletonSigning")
    public HashMap<String, String> getSingletonSigning() {
        return singletonSigning;
    }

    public void setSingletonSigning(HashMap<String, String> singletonSigning) {
        this.singletonSigning = singletonSigning;
    }

    @JsonProperty("counterSigning")
    public HashMap<String, String> getCounterSigning() {
        return counterSigning;
    }

    public void setCounterSigning(HashMap<String, String> counterSigning) {
        this.counterSigning = counterSigning;
    }
    
}