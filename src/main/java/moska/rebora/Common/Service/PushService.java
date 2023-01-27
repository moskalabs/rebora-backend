package moska.rebora.Common.Service;

import moska.rebora.User.Entity.User;

public interface PushService {

    void sendPush(String token, String title, String content);

    void sendUserPush(User user, String title, String content);
}
