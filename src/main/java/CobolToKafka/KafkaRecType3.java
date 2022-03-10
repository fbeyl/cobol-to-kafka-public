package CobolToKafka;

import com.ibm.jzos.fields.CobolDatatypeFactory;
import com.ibm.jzos.fields.StringField;

public class KafkaRecType3 {

    public static final int REC_Type3_LEN = 16;

    private byte[] bytes;

	private static CobolDatatypeFactory factory = new CobolDatatypeFactory();

 	//	03 RECTYPE                        PIC X.
    private static final StringField RecType = factory.getStringField(1);
 	//	03 TYPE-3-FIELD-1                 PIC X(15).
    private static final StringField Type3Field1 = factory.getStringField(15);

	public KafkaRecType3() {
		bytes = new byte[REC_Type3_LEN];
	}

	public KafkaRecType3(byte[] buffer) {
		setBytes(buffer);
	}

	/**
	 * Answer the underlying byte array mapped by this object.
	 * @return byte[]
	 */
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] buffer) {
		if ((buffer.length != REC_Type3_LEN) || (buffer[0] != (byte)0xF3)) {
			throw new IllegalArgumentException("wrong buffer length/record type");
		}
		bytes = buffer;
	}

	/**
	 * Get the RecType.
	 * <pre>
	 * 03  RECTYPE                      PIC X.
	 * </pre>
	 * @return String the RecType
	 */
	public String getRecType() {
		return RecType.getString(bytes);
	}

	/**
	 * @see #getRecType()
	 */
	public void setRecType(String recType) {
		RecType.putString(recType, bytes);
	}

	/**
	 * Get the Type3Field1.
	 * <pre>
	 * 	03 TYPE-3-FIELD-1                 PIC X(15).
	 * </pre>
	 * @return long the Type3Field1
	 */

	public String getType3Field1() {
		return Type3Field1.getString(bytes);
	}

	/**
	 * @see #getType3Field1()
	 */
	public void setType3Field1(String type3Field1) {
		Type3Field1.putString(type3Field1, bytes);
	}

}
