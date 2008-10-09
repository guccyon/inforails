########################################################################
#[概要] 送信メールIDに紐づくメールの内容を取得
#[詳細] 送信メールIDに紐づくメールの内容を取得
#[SQLID] GetMailContents
#
#[変更履歴]
#  日付        連番   名前      内容
# --------------------------------------------------
# 2006/10/03   VL000  樋口      新規
#
########################################################################
SELECT 
	 CONTENTS00
	,CONTENTS01
	,CONTENTS02
	,CONTENTS03
	,CONTENTS04
	,CONTENTS05
	,CONTENTS06
	,CONTENTS07
	,CONTENTS08
	,CONTENTS09
FROM
	 T_MAIL_MESSAGE
WHERE
	MAIL_ID = ?