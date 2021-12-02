using TodoListAPI.Models;
using TodoListAPI.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace BooksApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ChoresController : ControllerBase
    {
        private readonly GroupChoreService _service;

        public ChoresController(GroupChoreService service)
        {
            _service = service;
        }

        /*public ActionResult<List<Chore>> Get(string id)
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }
            else if (groupChore.Token != token)
            {
                return null;
            }

            return groupChore.Chores;
        }*/

        [HttpGet("{id:length(24)}", Name = "GetTask")]
        public ActionResult<Chore> GetTask(string id, string choreId)
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            else if (groupChore.Token != token)
            {
                return null;
            }

            return groupChore.Chores.Find(x => x.Id == choreId);
        }

        [HttpPost]
        public ActionResult<Chore> CreateTask(string id, Chore chore)
        {
            var groupChore = _service.Get(id);
            groupChore.Chores.Add(chore);
            _service.Update(id, groupChore);

            return CreatedAtRoute("GetTask", new { id = chore.Id.ToString() }, chore);
        }

        [HttpPut("{id:length(24)}")]
        public IActionResult UpdateTask(string id, string choreId, Chore choreIn)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            var chore = groupChore.Chores.Find(x => x.Id == choreId);
            if(chore == null)
            {
                return NotFound();
            }

            groupChore.Chores.Remove(chore);
            groupChore.Chores.Add(choreIn);

            _service.Update(id, groupChore);

            return NoContent();
        }

        [HttpDelete("{id:length(24)}")]
        public IActionResult DeleteTask(string id, string choreId)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            var chore = groupChore.Chores.Find(x => x.Id == choreId);
            if (chore == null)
            {
                return NotFound();
            }

            groupChore.Chores.Remove(chore);

            _service.Update(id, groupChore);

            return NoContent();
        }
    }
}

