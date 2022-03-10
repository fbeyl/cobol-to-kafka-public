package CobolToKafka;

import com.ibm.jzos.fields.CobolDatatypeFactory;
import com.ibm.jzos.fields.StringField;
import com.ibm.jzos.fields.BinaryAsIntField;

public class KafkaRecType4 {

    public static final int REC_Type4_LEN = 3;

    private byte[] bytes;

	private static CobolDatatypeFactory factory = new CobolDatatypeFactory();

 	//	03 RECTYPE                        PIC X.
    private static final StringField RecType = factory.getStringField(1);
 	//	03 TYPE-4-FIELD-1                 PIC 9(4) comp.
    private static final BinaryAsIntField Type4Field1 = factory.getBinaryAsIntField(0,false);

	public KafkaRecType4() {
		bytes = new byte[REC_Type4_LEN];
	}

	public KafkaRecType4(byte[] buffer) {
		setBytes(buffer);
	}

	public void setBytes(byte[] buffer) {
		if ((buffer.length != REC_Type4_LEN) || (buffer[0] != (byte)0xF4)) {
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
	 * Get the Type4Field1.
	 * <pre>
	 * 	03 TYPE-4-FIELD-1                 PIC 9(4) comp.
	 * </pre>
	 * @return int the Type4Field1
	 */

	public int getType4Field1() {
		return Type4Field1.getInt(bytes);
	}
}
