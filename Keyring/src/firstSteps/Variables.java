package firstSteps;

public enum Variables {
	INSTANCE;
	public final String keyFolder = "keypairs";
	public final byte[] iv = new byte[]{
            (byte)0x8E, 0x12, 0x39, (byte)0x9C,
            0x07, 0x72, 0x6F, 0x5A
        };
}
