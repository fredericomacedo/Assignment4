/***************************************************************************
 * File:  PojoCompositeListener.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group 07
 *   041029397, Frederico Lucio, Macedo
 *   041046587, Natalia, Pirath  
 *   041042876, Tongwe, Kasaji 
 *   041025651, Daniel, Barboza 
 */
package acmecollege.entity;

import static java.time.LocalDateTime.now;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoCompositeListener {

	@PrePersist
	public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		pojoBaseComposite.setCreated(now());
		pojoBaseComposite.setUpdated(now());
	}

	@PreUpdate
	public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		pojoBaseComposite.setUpdated(now());
	}

}
