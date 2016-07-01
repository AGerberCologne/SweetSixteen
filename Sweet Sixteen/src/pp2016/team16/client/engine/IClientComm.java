package pp2016.team16.client.engine;

import pp2016.team16.shared.MessageObject;

public interface IClientComm {
	public void bekommeVonClient(MessageObject cmsg);
	public MessageObject gebeWeiterAnClient();

}
