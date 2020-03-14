package JAssembly;

import java.util.Map;

public class OperandConvertor {
	public Short convertOperand(String operand, Map<String, String> constants, int lineNum) throws SyntaxException {
		if (operand.matches("[a-z]+")) {
			String constant = constants.get(operand);
			if (constant == null)
				throw new SyntaxException(lineNum, "Constant '" + constant + "' not found");
			return convertOperand(constant, constants, lineNum);
		}

		if (operand.matches("-?[0-9]+"))
			return convertToBytes(operand);

		if (operand.matches("r[0-9]+")) {
			int start = 0b1100000000000000;
			return (short) (start + convertToBytes(operand.substring(1)));
		}

		if (operand.matches("m[0-9]+")) {
			int start = 0b0100000000000000;
			return (short) (start + convertToBytes(operand.substring(1)));
		}

		if (operand.matches("m[+][0-9]+")) {
			int start = 0b1000000000000000;
			return (short) (start + convertToBytes(operand.substring(2)));
		}

		if (operand.matches("m[-][0-9]+")) {
			int start = 0b1000000000000000;
			return (short) (start + convertToBytes(operand.substring(1)));
		}

		throw new SyntaxException(lineNum, "Invalid form for operand '" + operand + "'");
	}

	public String convertToBinaryString(Short value) {
		String binary = Integer.toBinaryString(value);
		int diff = 16 - binary.length();
		if (diff > 0) {
			for (int i = 0; i < diff; i++)
				binary = "0" + binary;
		} else if (diff < 0) {
			binary = binary.substring(Math.abs(diff));
		}

		return binary;
	}

	public short extractValue(short value) {
		short stripper = (short) 0b0011111111111111;
		short stripped = (short) (value & stripper);
		short total = 0;
		int val = (int) Math.pow(2, 13);

		if (stripped > val) {
			total -= val;
			stripped -= val;
		}

		val /= 2;

		for (int i = 12; i >= 0; i--) {
			if (stripped >= val) {
				stripped -= val;
				total += val;
			}
			val /= 2;
		}

		return total;
	}

	public OperandType getType(short operand) {
		short memory = (short) 0b0100000000000000;
		short register = (short) 0b1100000000000000;
		short stripped = (short) (operand & register);

		if (stripped == 0)
			return OperandType.CONSTANT;

		if ((stripped ^ register) == 0)
			return OperandType.REGISTER;

		if ((operand & memory) != 0)
			return OperandType.MEMORY;

		return OperandType.MEMORYSHIFT;
	}

	private short convertToBytes(String data) {
		short value = Short.valueOf(data);
		int val = (int) Math.pow(2, 13);
		if (Math.abs(value) >= val)
			return value;
		short total = 0;

		if (value < 0) {
			value += val;
			total += val;
		}
		val /= 2;

		for (int i = 12; i >= 0; i--) {
			if (value >= val) {
				value -= val;
				total += val;
			}
			val /= 2;
		}
		return total;
	}
}