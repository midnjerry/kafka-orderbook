package games.crusader.orderbook.socket;

class SocketResponse
{
    public Boolean Success;
    public String ErrorCode;

    public SocketResponse(Boolean success, String error) 
    {
        Success = success;
        ErrorCode = error;
    }
}
