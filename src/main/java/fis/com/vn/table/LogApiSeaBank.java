package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "log_api_seabank", indexes = { @Index(name = "IDX_CODE_TRANSACTION", columnList = "code_transaction"),
		@Index(name = "IDX_PHONE", columnList = "phone"), @Index(name = "IDX_FULL_NAME", columnList = "full_name"),
		@Index(name = "IDX_ID_CONTRACT", columnList = "id_contract"),
		@Index(name = "IDX_DATE", columnList = "create_date"), @Index(name = "IDX_STATUS", columnList = "status"),
		@Index(name = "IDX_URI", columnList = "uri"), @Index(name = "IDX_ID_CARD", columnList = "id_card") })
@Data
public class LogApiSeaBank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	@Column(name = "log_id", length = 150)
	String logId;

	@Column(name = "time_handling")
	long timeHandling;

	@Column(name = "status")
	long status;

	@Column(name = "create_date")
	Date date;

	@Column(name = "token", length = 150)
	String token;

	@Column(name = "code", length = 150)
	String code;

	@Column(name = "uri")
	String uri;

	@Column(name = "method", length = 50)
	String method;

	@Column(name = "code_transaction", length = 150)
	String codeTransaction;

	@Column(name = "response", columnDefinition = "LONGTEXT")
	@Nationalized
	String response;

	@Column(name = "params", columnDefinition = "LONGTEXT")
	@Nationalized
	String params;

	@Column(name = "file_log")
	@Nationalized
	String fileLog;

	@Column(name = "phone", length = 150)
	String phone;

	@Column(name = "full_name", length = 150)
	String fullName;

	@Column(name = "id_card", length = 150)
	String idCard;

	@Column(name = "id_contract", length = 150)
	String idContract;

	@Column(name = "id_los", length = 150)
	String idLos;

	@Column(name = "mota", length = 150)
	String mota;
	
	@Column(name = "username", length = 150)
	String username;
}
