package jp.co.infonic.common.module.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * �ėpCSV�t�@�C���Ǎ����C�u����
 * 
 * �y�g�����z
 * CSVReader csv = new CSVReader(�ǂݍ��ݑΏۃt�@�C��).preLoadTitleRow(); //--��1
 * 
 * while(csv.next()) {
 *   String col1 = csv.get("column1");
 *   String col2 = csv.get("column2");
 * }
 * 
 * JDBC��ResultSet�̂悤�Ȋ��o�Ŏg�p���܂��B
 * 
 * ��1
 * CSV�t�@�C���̐擪�s���񖼂ɂȂ��Ă���ꍇ�̂�
 * preLoadTitleRow�����s���܂��B
 * �Ȃ��Ă��Ȃ��ꍇ�� new��A
 * setColumnName(String[] columns)�ŗ񖼂��Z�b�g����K�v������܂��B
 * 
 * 
 * @author higuchi
 *
 */
public class CSVReader {
	
	public static final String DEFAULT_ENCODE = "Windows-31J";
	
	private File file;
	
	private String encode;
	
	private String[] colmunName;
	
	private Iterator iter;
	
	private CSVRecordMap currentRow;
	
	private String defaultString;
	
	private int currentRowNum = 0;
	
	private boolean EOF = false;

	// �ǂݍ��ݍς݂�CSV�f�[�^��̍s��
	private int readBuffer = 1000;

	private int offset = 0;
	
	private int readIndex;
	
	public CSVReader(String fileName) {
		this(new File(fileName), DEFAULT_ENCODE);
	}
	
	public CSVReader(File file) {
		this(file, DEFAULT_ENCODE);
	}
	
	public CSVReader(File file, String encode) {
		this.file = file;
		this.encode = encode;
		iter = new LinkedList().iterator();
	}
	
	/**
	 * �t�@�C���̐擪�s���^�C�g����ǂݍ��݂܂�
	 * @return
	 * @throws IOException
	 */
	public CSVReader preLoadTitleRow() throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			CSVLineParser parser = readLine(br, new CSVLineParser());
			offset = readIndex;
			colmunName = parser.getSeparatedValues();
		} finally {
			if (br != null)
				br.close();
		}
		return this;
	}
	
	public void setColumnName(String[] title) {
		this.colmunName = title;
	}
	
	/**
	 * �J���������X�g���擾���܂�
	 * @return
	 */
	public String[] columnName() {
		return colmunName;
	}
	
	/**
	 * �t�@�C�����ǂ݂��Ă����s�����w�肷��B
	 * �t�@�C���̓ǂݍ��݂͖���open,close���s���ׁA
	 * ������x�o�b�t�@���Ă������������B
	 * �f�t�H���g��1000���R�[�h�B
	 * @param num
	 */
	public void setBufferSize(int num) {
		this.readBuffer = num;
	}
	
	/**
	 * ���݂̃��R�[�h���}�b�v�f�[�^�Ƃ��ĕԂ��B
	 * @return
	 */
	public CSVRecordMap currentRow() {
		return currentRow;
	}
	
	/**
	 * ���݂̃��R�[�h���t�@�C������ǂݍ��񂾕�����̂܂ܕԂ��B
	 * @return
	 */
	public String defaultString() {
		return defaultString;
	}
	
	/**
	 * ���݂̍s�ԍ����擾���܂��B
	 * @return
	 */
	public int currentRowNumber() {
		return currentRowNum;
	}
	
	/**
	 * ���̃��R�[�h�̗L����Ԃ��B
	 * @return
	 */
	public boolean next() {
		if (!iter.hasNext() && !EOF) {
			try {
				iter = readBuffer().iterator();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		}

		if (iter.hasNext()) {
			CSVLineParser parser = (CSVLineParser) iter.next();
			defaultString = parser.getDefaultString();
			currentRow = new CSVRecordMap();
			String[] cols = parser.getSeparatedValues();
			for (int i = 0; i < cols.length; i++)
				currentRow.put(colmunName[i], cols[i]);

			currentRowNum++;
			return true;
		} else {
			defaultString = null;
			currentRow = null;
			return false;
		}
	}
	
	/**
	 * �J�������ɑΉ����錻�݃��R�[�h�̒l��Ԃ��B
	 * @param columnName
	 * @return
	 */
	public String get(String columnName) {
		if (currentRow != null) {
			return (String) currentRow.get(columnName);
		}
		return null;
	}

	private List readBuffer() throws IOException {
		List result = new LinkedList();
		BufferedReader br = null;
		readIndex = 0;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
			seekOffset(br);

			while(result.size() < readBuffer) {
				CSVLineParser record = readLine(br, new CSVLineParser());
				if (EOF = record.isNull())
					break;
				else
					result.add(record);
			}
			offset = readIndex;
		} finally {
			if (br != null) br.close();
		}
		

		return result;
	}
	
	private CSVLineParser readLine(BufferedReader br, CSVLineParser parser) throws IOException {
		String line = br.readLine();
		readIndex++;
		if (line != null && !parser.appendString(line).isCorrectRecord()){
			return readLine(br, parser.appendString(System.getProperty("line.separator")));
		}
		
		return parser;
	}
	
	private void seekOffset(BufferedReader br) throws IOException {
		while (br.ready() && readIndex < offset) {
			br.readLine();
			readIndex++;
		}
	}
}
