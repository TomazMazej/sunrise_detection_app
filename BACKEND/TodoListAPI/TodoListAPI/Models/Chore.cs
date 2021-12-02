using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace TodoListAPI.Models
{
    public class Chore
    {
        public Chore()
        {
            AssignId();
        }
        
        public void AssignId()
        {
            // generiraj kljuc, ker je sub-document (in ga ne naredi avtomatsko)
            Id = ObjectId.GenerateNewId().ToString();
        }

        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        public string Name { get; set; }

        public string Description { get; set; }

        [BsonDateTimeOptions(Kind = DateTimeKind.Local)]
        public DateTime DueDate { get; set; }

        public bool IsCompleted { get; set; }
    }
}
