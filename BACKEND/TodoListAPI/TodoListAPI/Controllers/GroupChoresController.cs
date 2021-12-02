using TodoListAPI.Models;
using TodoListAPI.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace BooksApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GroupChoresController : ControllerBase
    {
        private readonly GroupChoreService _service;

        public GroupChoresController(GroupChoreService service)
        {
            _service = service;
        }

        [HttpGet]
        public ActionResult<List<GroupChore>> Get()
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groups = _service.Get().FindAll(x => x.Token == token);

            return groups;
        }

        [HttpGet("{id:length(24)}", Name = "GetGroup")]
        public ActionResult<GroupChore> Get(string id)
        {
            Request.Headers.TryGetValue("Authorization", out var token);

            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }
            else if(groupChore.Token != token)
            {
                return null;
            }

            return groupChore;
        }

        [HttpPost]
        public ActionResult<GroupChore> Create(GroupChore groupChore)
        {
            Request.Headers.TryGetValue("Authorization", out var token);
            
            groupChore.Token = token;
            _service.Create(groupChore);

            return CreatedAtRoute("GetGroup", new { id = groupChore.Id.ToString() }, groupChore);
        }

        [HttpPut("{id:length(24)}")]
        public IActionResult Update(string id, GroupChore groupChoreIn)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            _service.Update(id, groupChoreIn);

            return NoContent();
        }

        [HttpDelete("{id:length(24)}")]
        public IActionResult Delete(string id)
        {
            var groupChore = _service.Get(id);

            if (groupChore == null)
            {
                return NotFound();
            }

            _service.Remove(groupChore.Id);

            return NoContent();
        }
    }
}
