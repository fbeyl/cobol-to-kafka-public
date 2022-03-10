package CobolToKafka;

import com.ibm.jzos.fields.CobolDatatypeFactory;
import com.ibm.jzos.fields.StringField;
import com.ibm.jzos.fields.PackedDecimalAsLongField;

public class KafkaRecType1 {

    public static final int REC_TYPE1_LEN = 10;

    private byte[] bytes;

	private static CobolDatatypeFactory factory = new CobolDatatypeFactory();

 	//	03 RECTYPE                        PIC X.
    private static final StringField RecType = factory.getStringField(1);
 	//	03 TYPE-1-FIELD-1                 PIC S9(17) COMP-3.
    private static final PackedDecimalAsLongField Type1Field1 = factory.getPackedDecimalAsLongField(17, true);

	public KafkaRecType1() {
		bytes = new byte[REC_TYPE1_LEN];
	}

	public KafkaRecType1(byte[] buffer) {
		setBytes(buffer);
	}

	public void setBytes(byte[] buffer) {
		if ((buffer.length != REC_TYPE1_LEN) || (buffer[0] != (byte)0xF1)) {
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
	 * Get the Type1Field1.
	 * <pre>
	 * 03  TYPE-1-FIELD-1               PIC S9(17) COMP-3.
	 * </pre>
	 * @return long the Type1Field1
	 */

	public long getType1Field1() {
		return Type1Field1.getLong(bytes);
	}

}
