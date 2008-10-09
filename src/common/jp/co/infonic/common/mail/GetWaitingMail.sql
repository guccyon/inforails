########################################################################
#[概要] 送信待ち状態であるメールを取得します。
#[詳細] 送信待ち状態であるメールを取得します。
#[SQLID] GetWaitingMail
#
#[変更履歴]
#  日付        連番   名前      内容
# --------------------------------------------------
# 2006/10/03   VL000  樋口      新規
#
########################################################################
SELECT 
	 DISTINCT AD.MAIL_ID
	 ,AD.FROM_ADDR
	 ,AD.FROM_NAME
	 ,AD.REPLY_TO
	 ,AD.REPLY_NAME
	 ,AD.PRIORITY 
	 ,ME.ATTACH_FILE_ID
	 ,ME.SUBJECT
FROM
	T_MAIL_ADDRESS AD,
	T_MAIL_MESSAGE ME
WHERE
	AD.SOSHIN_DATE <= SYSDATE
	AND
	AD.SOSHIN_FLG = 0
	AND 
	AD.MAIL_ID = ME.MAIL_ID 
ORDER BY 
	PRIORITY DESC,
	MAIL_ID ASC