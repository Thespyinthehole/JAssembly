package JAssembly;

import java.util.Map;

import JAssembly.Compiler.Constant;

public class OperandConvertor {
	
	public static Short convertOperand(String operand, Map<String, Constant> constants, int lineNum) throws SyntaxException {
		if (operand.matches("[a-z]+")) {
			Constant constant = constants.get(operand);
			if (constant == null)
				throw new SyntaxException(lineNum, "Constant '" + operand + "' not found");
			constant.use();
			return convertOperand(constant.getValue(), constants, lineNum);
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

	public static String convertToBinaryString(Short value) {
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

	public static short extractValue(short value) {
		short constant = (short) 0b1100000000000000;
		short stripper = (short) 0b0011111111111111;
		short stripped = (short) (value & stripper);

		if((value & constant) != 0)
			return stripped;

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

	public static OperandType getType(short operand) {
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

	private static short convertToBytes(String data) {
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
