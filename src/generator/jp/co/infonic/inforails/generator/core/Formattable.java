package jp.co.infonic.inforails.generator.core;

import java.util.List;

/**
 * 整形された書式で出力する為のインターフェースを持つ
 *
 */
public interface Formattable {

	// 複数要素をリストとして返す
	List <String> toFormatLines();
}
