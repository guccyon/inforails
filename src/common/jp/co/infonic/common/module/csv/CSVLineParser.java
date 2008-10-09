package jp.co.infonic.common.module.csv;

import java.util.LinkedList;

/**
 * CSV�P�s���̃��R�[�h����͂���N���X
 * @author higuchi
 *
 */
public class CSVLineParser {

	public static final char DEFAULT_DELIMITER = ',';
	
	private StringBuffer buffer;
	
	int quotNum = 0;
	
	/**
	 * �P���R�[�h���̃f�[�^�ɕ������ǉ�����
	 * @param line
	 */
	CSVLineParser appendString(String line) {
		if (buffer == null) buffer = new StringBuffer();
		
		countQuot(line);
		buffer.append(line);
		return this;
	}
	
	/**
	 * �P���R�[�h���̃f�[�^����������͂ł���f�[�^���Ԃ�
	 * @return
	 */
	boolean isCorrectRecord() {
		return isEven(quotNum);
	}
	
	/**
	 * �P���R�[�h���̕�������J���}�ŕ������z��ɂ��ĕԂ��B
	 * @return
	 */
	String[] getSeparatedValues() {
		return getSeparatedValues(DEFAULT_DELIMITER);
	}
	
	/**
	 * �P���R�[�h���̕������񂲂ƂɃf���~�^���w��ŕ������z��ɂ��ĕԂ��B
	 * @param delimiter �f���~�^
	 * @return
	 */
	String[] getSeparatedValues(char delimiter) {
		String[] cols = splitTokens(String.valueOf(delimiter));
		for (int i = 0; i < cols.length; i++)
			cols[i] = trim(cols[i]).replaceFirst("^\"", "").replaceFirst("\"$", "");
		return cols;
	}
	
	/**
	 * �ǂݍ��񂾂܂܂̂P�s���̕������Ԃ�
	 * @return
	 */
	String getDefaultString() {
		return buffer.toString();
	}
	
	boolean isNull() {
		return buffer == null;
	}
	
	// �o�b�t�@���̕������񂲂Ƃɕ�������
	private String[] splitTokens(String delimiter) {
		LinkedList tokens = new LinkedList();
		
		String[] splits = buffer.toString().split(delimiter, -1);
		for (int i = 0; i < splits.length; i++) {
			if (tokens.isEmpty() || isCorrectElement((String)tokens.getLast())) {
				tokens.add(splits[i]);
			} else {
				tokens.add( tokens.removeLast() + delimiter + splits[i] );
			}
		}
		
		return (String[])tokens.toArray(new String[0]);
	}
	
	// ��v�f�̃f�[�^�����������`�F�b�N����
	private boolean isCorrectElement(String str) {
		return isEven(countChar(str, '"', 0));
	}
	
	// �����񒆂̃_�u���N�H�[�e�[�V�����̐��𐔂���
	private int countQuot(String str) {
		return quotNum += countChar(str, '"', 0);
	}
	
	// �����񒆂̃L�����i�����j�̐��𐔂���
	private int countChar(String str, char c, int start) {
		int i = 0, index = str.indexOf(c, start);
		return index == -1 ? i : countChar(str, c, index + 1) + 1;
	}
	
	// ��������g���~���O����
	private String trim(String str){
		return str.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
	}
	
	private boolean isEven(int value) {
		return value % 2 == EVEN;
	}
	private boolean isOdd(int value) {
		return value % 2 == ODD;
	}
	
	private static final int ODD = 1;
	private static final int EVEN = 0;
}
