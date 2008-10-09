package jp.co.infonic.common.module.ex;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * ”z—ñŠg’£ƒNƒ‰ƒX
 * @param <E>
 */
public class ExArray <E> extends ExObject{
	private E[] ary;
	
	public static BreakException BREAK = new BreakException();
	
	public ExArray(E[] ary) {
		this.ary = ary;
	}
	
	public ExArray(List<E> list, E[] type) {
		this.ary = list.toArray(type);
	}
	
	public ExArray(List<E> list) {
		this.ary = (E[])list.toArray();
	}

	public void breakEnd() { throw BREAK; }
	
	public LinkedList<E> toList() {
		final LinkedList<E> result = new LinkedList<E>();
		each(new Iterator<E>(){
			public void yield(int i, E obj) { result.add(obj); }
		});
		return result;
	}
	
	public E[] toArray() { return ary; }
	
	public int length() { return ary.length; }
	
	@Override
	public boolean isExist() { return length() > 0; }
	
	public boolean each(Iterator<E> iter) {
		try {
			for(int i = 0; i < ary.length; i++) iter.yield(i, ary[i]);
		} catch(BreakException e){ return false; }
		return true;
	}
	
	public String inject(String memorize, final ReturnDynamicIterator<E> iter) {
		return (String)inject((Object)memorize, iter);
	}
	
	public Object inject(Object memorize, final ReturnDynamicIterator<E> iter) {
		final ThreadLocal<Object> tl = new ThreadLocal<Object>();
		tl.set(memorize);
		each(new Iterator<E>(){
			public void yield(int i, E element) {
				tl.set( iter.yield(i, element, tl.get()) );
			}
		});
		return tl.get();
	}
	
	public String join(final String delimiter) {
		return this.inject("", new ExArray.ReturnDynamicIterator<E>(){
				public Object yield(int i, E e, Object... obj) {
					return obj[0] + (i == 0 ? "" : delimiter) + e;
				}
			}
		).toString();
	}
	 
	public ExArray<E> push(E element) {
		List<E> result = toList();
		result.add(element);
		return new ExArray<E>(result);
	}
	
	public E first() {
		return ary[0];
	}
	
	public ExArray<E> last() {
		LinkedList<E> result = toList();
		result.removeFirst();
		return new ExArray<E>(result);
	}
	
	public ExArray<Object> map(final ReturnIterator<E> iter) {
		final List<Object> result = new LinkedList<Object>();
		each(new Iterator<E>(){
			public void yield(int i, E e) {	result.add(iter.yield(i, e)); }
		});
		return ExA(result.toArray(new Object[0]));
	}
	
	public E detect(final ResultIterator<E> iter) {
		final ThreadLocal<E> tl = new ThreadLocal<E>();
		each(new Iterator<E>(){
			public void yield(int i, E e) {	
				if (iter.yield(i, e)) { tl.set(e);	breakEnd(); }
			}
		});
		return tl.get();
	}
	
	public boolean isInclude(final E target) {
		return detect(new ResultIterator<E>(){
			public boolean yield(int i, E e) { return target.equals(e);	}
		}) != null;
	}
	
	public boolean all(final ResultIterator<E> iter) {
		return each(new Iterator<E>(){
			public void yield(int i, E e) {
				if (!iter.yield(i, e)) breakEnd();
			}
		});
	}
	
	public boolean any(final ResultIterator<E> iter) {
		return !each(new Iterator<E>(){
			public void yield(int i, E e) {
				if (iter.yield(i, e)) breakEnd();
			}
		});
	}
	
	public void bubbleSort_(Comparator<E> comp) {
		for(int i = 0; i < ary.length - 1; i++)
			for(int j = i + 1; j < ary.length; j++)
				if (comp.compare(ary[i], ary[j]) > 0) {
					E temp = ary[i];
					ary[i] = ary[j];
					ary[j] = temp;
				}
	}
	
	
	public static interface Iterator<T> {
		public void yield(int i, T e);
	}
	
	public static interface ResultIterator<T> {
		public abstract boolean yield(int i, T e);
	}
	
	public static interface StatusIterator<T> {
		public abstract int yield(int i, T e);
	}
	
	public static interface ReturnIterator<T> {
		public abstract Object yield(int i, T e, Object ... obj);
	}
	
	public static interface ReturnDynamicIterator<T> {
		public abstract Object yield(int i, T e, Object ... obj);
	}
	
	public static ExArray<Object> ExA(Object[] ary) {
		return new ExArray<Object>(ary);
	}
	
	public static ExArray<Object> ExA(List<Object> list) {
		return new ExArray<Object>(list.toArray(new Object[0]));
	}
}
