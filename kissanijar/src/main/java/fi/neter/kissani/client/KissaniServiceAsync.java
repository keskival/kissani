package fi.neter.kissani.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fi.neter.kissani.shared.CatTO;
import fi.neter.kissani.shared.ProfileTO;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("kis")
public interface KissaniServiceAsync {
    public void setDefaultPhoto(Long catId, Long photoId, AsyncCallback<Void> callback);
    
    public void canEditPerson(Long id, AsyncCallback<Boolean> callback);

    public void canEditCat(CatTO cat, AsyncCallback<Boolean> callback);

    public void getCat(Long catId, AsyncCallback<CatTO> callback);
    
    public void getCatsOrCreate(Long id, AsyncCallback<Collection<Long>> callback);
    
    public void getCats(Long id, AsyncCallback<Collection<Long>> callback);
    
    public void persistCat(CatTO cat, AsyncCallback<Void> callback);
    
    public void editCat(Long id, String nickName, String name, AsyncCallback<Void> callback);

    public void deleteCat(Long id, AsyncCallback<Void> callback);

    public void removeOwnerFromCat(Long catId, Long ownerId, AsyncCallback<Void> callback);

    public void removePhoto(Long catId, Long photoId, AsyncCallback<Void> callback);
    
    public void addCat(String nickName,
            String name, AsyncCallback<Void> callback);

    public void tagScan(Long id, AsyncCallback<Void> callback);

	public void addFriendForCat(Long catId, Long friendId, AsyncCallback<Void> callback);

	public void addOwnerForCat(Long ownerId, Long catId, AsyncCallback<Void> callback);

	public void getProfile(String id,
			AsyncCallback<ProfileTO> asyncCallback);
}
