final class Event
{
   private Action action;
   private long time;
   private AbstractEntity entity;

   public Event(Action action, long time, AbstractEntity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   public Action getAction() {
      return action;
   }

   public long getTime() {
      return time;
   }

   public AbstractEntity getEntity() {
      return entity;
   }
}
