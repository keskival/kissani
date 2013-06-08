package fi.neter.kissani.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fi.neter.kissani.shared.CatTO;
import fi.neter.kissani.shared.ProfileTO;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("kis")
public interface KissaniService extends RemoteService {
    public void setDefaultPhoto(Long catId, Long photoId);
    
    public Boolean canEditPerson(Long id);

    public Boolean canEditCat(CatTO cat);

    public CatTO getCat(Long catId);
    
    public Collection<Long> getCatsOrCreate(Long id);
    
    public Collection<Long> getCats(Long id);
    
    public void persistCat(CatTO cat);
    
    public void editCat(Long id, String nickName, String name);

    public void deleteCat(Long id);

    public void removeOwnerFromCat(Long catId, Long ownerId);

    public void removePhoto(Long catId, Long photoId);
    
    public void addCat(String nickName,
            String name);

    public void tagScan(Long id);

	public void addFriendForCat(Long catId, Long friendId);

	public void addOwnerForCat(Long ownerId, Long catId);

	public ProfileTO getProfile(String id);
}
