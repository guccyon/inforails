########################################################################
#[�T�v] ���M�������[���̑��M�ς݃t���O�𑗐M�ς݂ɕύX
#[�ڍ�] ���M�������[���̑��M�ς݃t���O�𑗐M�ς݂ɕύX
#[SQLID] UpdateSendFlg
#
#[�ύX����]
#  ���t        �A��   ���O      ���e
# --------------------------------------------------
# 2006/10/03   VL000  ���      �V�K
#
########################################################################
UPDATE 
	T_MAIL_ADDRESS
SET
	SOSHIN_FLG = 1
WHERE
	MAIL_ID = ?
	AND
	SOSHIN_DATE <= SYSDATE
	AND
	SOSHIN_FLG = 0