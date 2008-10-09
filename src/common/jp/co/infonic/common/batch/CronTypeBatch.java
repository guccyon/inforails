package jp.co.infonic.common.batch;

import java.util.Calendar;

public abstract class CronTypeBatch extends CnkBatch {
	
	private static final int MINUTE = 1;
	private static final int MINUTE_FOR_HOUR = MINUTE * 60;
	private static final int MINUTE_FOR_DAY = MINUTE_FOR_HOUR * 24;

	private int minute;
	private int hour;
	private int day;
	private int month;
	private int weekday;
	
	public CronTypeBatch(String minute, String hour, String day, String month, String weekday) {
		super(1);
		int[] checker = new int[5];
		if ("*".equals(minute)) {
			this.minute = -1;
		} else {
			this.minute = Integer.parseInt(minute);
			if (this.minute > 59 || this.minute < 0) {
				throw new IllegalArgumentException();
			}
		}
		if ("*".equals(hour)) {
			this.hour = -1;
		} else {
			this.hour = Integer.parseInt(hour);
			if (this.hour > 23 || this.hour < 0) {
				throw new IllegalArgumentException();
			}
		}
		if ("*".equals(day)) {
			this.day = -1;
		} else {
			this.day = Integer.parseInt(day);
			if (this.day > 31 || this.day < 0) {
				throw new IllegalArgumentException();
			}
		}
		if ("*".equals(month)) {
			this.month = -1;
		} else {
			this.month = Integer.parseInt(month);
			if (this.month > 12 || this.month < 0) {
				throw new IllegalArgumentException();
			}
		}
		if ("*".equals(weekday)) {
			this.weekday = -1;
		} else {
			this.weekday = Integer.parseInt(weekday);
			if (this.weekday > 6 || this.weekday < 0) {
				throw new IllegalArgumentException("�j���͂O�i���j�`�U�i�y�j�Ŏw�肵�Ă��������B");
			}
		}
		
		if (this.weekday != -1 && (this.day != -1 || this.month != -1)) {
			throw new IllegalArgumentException("���t�Ɨj���𓯎��Ɏw�肷�鎖�͂ł��܂���B");
		}
		
		reset();
	}

	/**
	 * �b�����
	 * ���݂̎����ł́A���s�L���[�̐�s�^�X�N�ɏd�����������܂܂�Ă����ꍇ�A
	 * ��������΂����\��������B
	 * 
	 * ���Ԃ͈͎̔w���A�J���}��؂�ł̕����w��ɖ��Ή�
	 */
	boolean isExeTime(long current) {
		Calendar cal = Calendar.getInstance();
		
		boolean result = true;
		result = result && (this.minute == -1 || this.minute == cal.get(Calendar.MINUTE));
		
		result = result && (this.hour == -1 || this.hour == cal.get(Calendar.HOUR_OF_DAY));
		
		result = result && (this.day == -1 || this.day == cal.get(Calendar.DATE));
		
		result = result && (this.month == -1 || this.month == (cal.get(Calendar.MONTH) + 1));

		result = result && (this.weekday == -1 || this.weekday == getWeekDayInt(cal));
		
		return result;
	}

	protected void reset() {
		Calendar now = Calendar.getInstance();
		int after = 0;
		int tempMinute = 1;
		if (this.minute != -1) {
			tempMinute = this.minute - now.get(Calendar.MINUTE);
			after += tempMinute;
			if (tempMinute < 0) {
				after += MINUTE_FOR_HOUR;
			}
		} else {
			after++;
		}
		
		int tempHour = 0;
		if (this.hour != -1) {
			tempHour = this.hour - now.get(Calendar.HOUR_OF_DAY);
			if (tempMinute < 0) {
				tempHour -= 1;
			}
			
			if (tempHour < 0) {
				after += MINUTE_FOR_DAY;
			}
			after += tempHour * MINUTE_FOR_HOUR;
		}
		
		int tempDays = getDays(now, (tempHour < 0));
		after += tempDays * MINUTE_FOR_DAY;
		
		if (after == 0) {
			after = 1;
		}
		setNextTime(after);
	}
	
	private int getDays(Calendar now, boolean past) {

		int tempDays = 0;
		if (this.day != -1) {
			if (this.month != -1) {
				int tempDay = this.day;
				if (past) {
					tempDay -= 1;
				}
				tempDays = dayFromTodayToNext(this.month, tempDay);
			} else {
				int curD = now.get(Calendar.DATE);
				if (past) {
					curD += 1;
				}
				if (curD > this.day) {
					tempDays = dayFromTodayToNext(nextMonth(now), this.day);
				} else {
					tempDays = dayFromTodayToNext(now.get(Calendar.MONTH) + 1, this.day);
				}
			}
		} else if (this.month != -1) {
			if ((now.get(Calendar.MONTH) + 1) != this.month) {
				tempDays = dayFromTodayToNext(this.month, 1);
			}
		} else {
			if (this.weekday != -1) {
				int week = getWeekDayInt(now);
				tempDays = this.weekday - week;
				if (past) {
					tempDays -= 1;
				}
				if (tempDays < 0) {
					tempDays += 7;
				}
			}
		}
		return tempDays;
	}
	
	// ����
	private int nextMonth(Calendar now) {
		int month = now.get(Calendar.MONTH) + 1;
		if (month == 12) {
			return 1;
		}
		return month;
	}
	
	// �w����܂ł̓��������߂�
	private int dayFromTodayToNext(int targetM, int targetD) {
		Calendar now = Calendar.getInstance();
		int targetY = now.get(Calendar.YEAR);
		if ((now.get(Calendar.MONTH) + 1) > targetM) {
			targetY++;
		} else if ((now.get(Calendar.MONTH) + 1) == targetM && now.get(Calendar.DATE) > targetD) {
			targetY++;
		}
		
		long current = now.getTimeInMillis();
		now.set(targetY, targetM - 1, targetD);
		
		long target = now.getTimeInMillis();
		long mills = target - current;
		int days = (int)(mills / (1000 * 60 * MINUTE_FOR_DAY));
		return days;
	}
	
	/**
	 * �T�v: Calendar�N���X���\�����t�̗j����CronTypeBatch�N���X�̎d�l�Ɋ�Â��擾����<br>
	 * �ڍ�: CronTypeBatch�N���X�ł͂O�i���j�`�U�i�y�j�Ƃ����d�l�����邪�A<br>
	 *       Calendar#get(Calendar.DAY_OF_WEEK)�Ŏ擾����l�͖��m�Ɏd�l��<br>
	 *       ���܂��Ă��Ȃ����߂��̂悤�ȕϊ���Ƃ��K�v<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param cal
	 * @return
	 */
	private int getWeekDayInt(Calendar cal){
		int result = -1;
		
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			result = 0;
			break;

		case Calendar.MONDAY:
			result = 1;
			break;

		case Calendar.TUESDAY:
			result = 2;
			break;
			
		case Calendar.WEDNESDAY:
			result = 3;
			break;
			
		case Calendar.THURSDAY:
			result = 4;
			break;
			
		case Calendar.FRIDAY:
			result = 5;
			break;
			
		case Calendar.SATURDAY:
			result = 6;
			break;
		}
		
		return result;
	}
	
}
