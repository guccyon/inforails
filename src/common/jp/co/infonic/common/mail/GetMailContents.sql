########################################################################
#[�T�v] ���M���[��ID�ɕR�Â����[���̓��e���擾
#[�ڍ�] ���M���[��ID�ɕR�Â����[���̓��e���擾
#[SQLID] GetMailContents
#
#[�ύX����]
#  ���t        �A��   ���O      ���e
# --------------------------------------------------
# 2006/10/03   VL000  ���      �V�K
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