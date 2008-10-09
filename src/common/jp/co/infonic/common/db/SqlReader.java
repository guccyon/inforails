package jp.co.infonic.common.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class SqlReader {
	
	private List list = new LinkedList();
	
	private BufferedReader br;
	
	private int i = 0;
	
	public SqlReader(InputStream in) throws IOException {
		this(in, "Windows-31J");
	}
	
	public SqlReader(InputStream in, String encode) throws IOException {
		this(new InputStreamReader(in, encode));
	}
	
	public SqlReader(InputStreamReader in) throws IOException {
		br = new BufferedReader(in);
		try {
			readAll();
		} finally {
			br.close();	
		}
	}
	
	/**
	 * ����SQL���ǂݍ��߂邩�Ԃ�
	 * @return
	 */
	public boolean hasNext() {
		return list.size() > i;
	}
	
	/**
	 * ����SQL���擾����B
	 * @return
	 */
	public String nextSQL() {
		return (String)list.get(i++);
	}
	
	/**
	 * �ǂݍ���SQL�̐���Ԃ�
	 * @return
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * �s�R�����g�F�@// or -- or �擪�� #
	 * �����s�R�����g�F�@/* �`�@*\/
	 * �s�̍Ō��;�i�Z�~�R�����j���͐擪��/�i�X���b�V���j��
	 * �������ꍇ��SQL�̏I���Ɣ��f���A
	 * ���̍s���������SQL�Ƃ��ēǂݍ��݂܂��B
	 * @throws IOException
	 */
	private void readAll() throws IOException {
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = br.readLine()) != null) {
			
			if (line.matches("^\\s*#.*")) continue;
			
			if (line.indexOf("//") != -1) line = line.replaceFirst("//.*", "");
			
			if (line.indexOf("--") != -1) line = line.replaceFirst("--.*", "");
			
			if (line.matches(".*;\\s*$") || line.matches("/.*")) {
				line = line.replaceFirst(";\\s*$", "").replaceFirst("/.*", "");
				sb.append(line);
				list.add(fomat(sb.toString()));
				sb = new StringBuffer();
			} else {
				sb.append(line).append(" ");	
			}
		}
		
		if (sb.length() > 0) {
			list.add(fomat(sb.toString()));
		}
	}
	
	/**
	 * �]���ȃX�y�[�X�A�R�����g�A�E�g����������B
	 */
	private String fomat(String sql) {
		sql = sql.replaceAll("\t", " ");
		sql = sql.replaceAll("/\\*.*?(\\*/|$)", "");
		while(sql.indexOf("  ") != -1) {
			sql = sql.replaceFirst("  ", " ");
		}
		sql = sql.replaceAll(" ,|, ", ",");
		return sql.trim();
	}
}
