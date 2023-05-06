/**
 * 
 */
package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;
import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Entity
@Data
@Table(name = "log_code_transaction", indexes = { 
		@Index(name = "IDX_CODE_TRANSACTION", columnList = "code_transaction"),
		@Index(name = "IDX_PHONE", columnList = "phone"),
		@Index(name = "IDX_FULL_NAME", columnList = "full_name"),
		@Index(name = "IDX_ID_CONTRACT", columnList = "id_contract"),
		@Index(name = "IDX_DATE", columnList = "date"), 
		@Index(name = "IDX_ID_CARD", columnList = "id_card") 
		})
public class LogCodeTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Column(name = "code_transaction", length = 150)
	String codeTransaction;
	
	@Column(name = "date_string", length = 150)
	String dateString;
	
	@Column(name = "date_string_hour", length = 150)
	String dateStringHour;
	
	@Column(name = "code", length = 150)
	String code;
	
	@Column(name = "date")
	Date date;
	
	@Column(name = "phone", length = 150)
	String phone;
	
	@Column(name = "full_name", length = 150)
	String fullName;
	
	@Column(name = "id_card", length = 150)
	String idCard;
	
	@Column(name = "id_contract", length = 150)
	String idContract;
}
