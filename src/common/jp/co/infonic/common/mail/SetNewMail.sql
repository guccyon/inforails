########################################################################
#[�T�v] ���b�Z�[�W�e�[�u���ɐV�K���[����ǉ����܂��B
#[�ڍ�] ���b�Z�[�W�e�[�u���ɐV�K���[����ǉ����܂��B
#[SQLID] SetNewMail
#
#[�ύX����]
#  ���t        �A��   ���O      ���e
# --------------------------------------------------
# 2006/10/03   VL000  ���      �V�K
#
########################################################################
INSERT INTO T_MAIL_MESSAGE ( 
	 MAIL_ID       
	,SUBJECT       
	,ATTACH_FILE_ID
	,CONTENTS00    
	,CONTENTS01    
	,CONTENTS02    
	,CONTENTS03    
	,CONTENTS04    
	,CONTENTS05    
	,CONTENTS06    
	,CONTENTS07    
	,CONTENTS08    
	,CONTENTS09
) VALUES (
	 ?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
	,?
)
