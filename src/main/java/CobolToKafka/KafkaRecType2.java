package CobolToKafka;

import com.ibm.jzos.fields.CobolDatatypeFactory;
import com.ibm.jzos.fields.StringField;
import com.ibm.jzos.fields.ExternalDecimalAsIntField;

public class KafkaRecType2 {

    public static final int REC_Type2_LEN = 3;

    private byte[] bytes;

	private static CobolDatatypeFactory factory = new CobolDatatypeFactory();

 	//	03 RECTYPE                        PIC X.
    private static final StringField RecType = factory.getStringField(1);
 	//	03 TYPE-2-FIELD-1                 PIC S99.
    private static final ExternalDecimalAsIntField Type2Field1 = factory.getExternalDecimalAsIntField(2,true);

	public KafkaRecType2() {
		bytes = new byte[REC_Type2_LEN];
	}

	public KafkaRecType2(byte[] buffer) {
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
		if ((buffer.length != REC_Type2_LEN) || (buffer[0] != (byte)0xF2)) {
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
	 * Get the Type2Field1.
	 * <pre>
	 * 	03 TYPE-2-FIELD-1                 PIC S99.
	 * </pre>
	 * @return long the Type2Field1
	 */

	public long getType2Field1() {
		return Type2Field1.getInt(bytes);
	}

	/**
	 * @see #getType2Field1()
	 */
	public void setType2Field1(int type2Field1) {
		Type2Field1.putInt(type2Field1, bytes);
	}

}
