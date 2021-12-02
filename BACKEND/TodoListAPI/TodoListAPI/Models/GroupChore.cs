using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace TodoListAPI.Models
{
    public class GroupChore
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public List<Chore> Chores { get; set; }

        public GroupChore()
        {
            Chores = new();
        }
    }
}
