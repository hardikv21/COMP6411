-module(exchange).
-author("hardi").

-export([get_feedback2/1, start/0]).

start()->
  Temp = lists:nth(2, tuple_to_list(file:consult("calls.txt"))),
  io:fwrite("** Calls to be made **~n"),
  lists:foreach(fun(N) ->
          N1 = tuple_to_list(N),
          Name = lists:nth(1, N1),
          io:fwrite("~w:", [Name]),
          io:fwrite(" ~w~n", [lists:nth(2, N1)])
  end, Temp),
  io:fwrite("~n"),
  lists:foreach(fun(N) ->
        N1 = tuple_to_list(N),
        Name = lists:nth(1, N1),
        Contact = lists:nth(2, N1),
        Pid = spawn(calling, baz2, [Name, Contact,self(), 0]),
        register(Name,Pid)
    end, Temp),
  get_feedback2(0).


get_feedback2(Counter)->
  receive
    {intro,Name,Sender,Timestamp} ->
      io:fwrite("~p received intro message from ~p [~p]~n",[Name,Sender,Timestamp]),
      get_feedback2(Counter+1);
    {reply,Name,Sender,Timestamp} ->
      io:fwrite("~p received reply message from ~p [~p]~n",[Name,Sender,Timestamp]),
      get_feedback2(Counter+1)
  after
    10000->
      io:fwrite("~nMaster has received no replies for 10 seconds, ending...\n",[])
  end.