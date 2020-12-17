
import java.util.LinkedList;

/**
 * @author Nathan
 * 
 *This class defines a cache data structure which holds and manages generic data
 *
 * @param <T>
 */
public class Cache<T> {

	private int currentSize, maxSize;
	private LinkedList<T> cache;
	
	/**
	 * Constructor method for the cache given a specified size
	 * @param size
	 */
	public Cache(int size) {
		
		this.maxSize = size;
		currentSize = 0;
		cache = new LinkedList<T>();
		
	}
	/**
	 * This method adds the specified object to the cache and updates the size of the cache
	 * If the current size of the cache goes over the max size, the bottom element is removed
	 * @param element
	 */
	public void addObject(T element) {
		
		if(currentSize < maxSize) {
			
		cache.add(element);
		currentSize++;
		
		} else {
			
			cache.removeFirst();
			cache.add(element);
			
		}
		
	}
	/**
	 * This method removes a specified object from the cache
	 * @param element
	 */
	public void removeObject(T element) {
		
		cache.remove(element);
		currentSize--;
		
	}
	
	/**
	 * This method returns true or false if an object is found or not in the cache
	 * @param element
	 * @return true if element is found in the cache
	 */
	public boolean getObject(T element) {
		
		return cache.contains(element);
		
	}
	
	/**
	 * This method clears the cache
	 */
	public void clearCache() {
		
		cache = new LinkedList<T>();
		currentSize = 0;
		
	}
	
	/**
	 * This method moves a specified element to the top of the cache
	 * @param element
	 */
	public void addToTop(T element) {
		
		cache.remove(element);
		cache.add(element);
		
	}
	
	
}
