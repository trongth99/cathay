package fis.com.vn.esigncloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;

import vn.mobileid.exsig.SigningMethodAsync;

public class SigningMethodAsyncImp implements  SigningMethodAsync{
    public List<String> certificateChain;
    public List<String> signatures;
    public List<String> hashList;

    public List<String> getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public List<String> getHashList() {
        return hashList;
    }

    public void setHashList(List<String> hashList) {
        this.hashList = hashList;
    }
    
    
    public void saveTemporalData(String owner, byte [] temporalData) throws IOException {
        String result = System.getProperty("java.io.tmpdir");
        String fileName = result + owner + ".temp";
        FileUtils.writeByteArrayToFile(new File(fileName), temporalData);
    }
    
    public byte [] loadTemporalData(String owner) throws IOException {
        String result = System.getProperty("java.io.tmpdir");
        String fileName = result + owner + ".temp";
        return Files.readAllBytes(Paths.get(fileName));
    }

	public void generateTempFile(List<String> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<String> getCert() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> pack() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
