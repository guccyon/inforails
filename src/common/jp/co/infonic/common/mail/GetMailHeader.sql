########################################################################
#[概要] 送信メールIDに紐づくメールのヘッダ情報を取得
#[詳細] 送信メールIDに紐づくメールのヘッダ情報を取得
#[SQLID] GetMailHeader
#
#[変更履歴]
#  日付        連番   名前      内容
# --------------------------------------------------
# 2006/10/03   VL000  樋口      新規
#
########################################################################
SELECT 
	 MIME_HEADER
	,MIME_HEADER_VALUE
FROM
	T_MAIL_HEADER
WHERE
	MAIL_ID = ?