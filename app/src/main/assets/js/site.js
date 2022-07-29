// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.

var uri = 'http://medicall-002-site5.ctempurl.com/api/UserSpotOns';
let todos = [];

function getItems() {

    fetch("http://medicall-002-site5.ctempurl.com/api/UserSpotOns?page=0&pageSize=10")
        .then(response => response.json())
        .then(data => _displayItems(data))
        .catch(error => console.error('Unable to get items.', error)); 
}

function getSpecialItems() {
const addNameTextbox = document.getElementById('add-name');
    fetch(uri+"/GetSpecialDoctors?special="+addNameTextbox.value.trim()+"&page=0&pageSize=10")
        .then(response => response.json())
        .then(data => _displayItems(data))
        .catch(error => console.error('Unable to get items.', error));
 
}

function authenticate() {
const addNameTextbox = document.getElementById('add-name');
    fetch(uri+"/authenticateuser?username="+addNameTextbox.value.trim()+"&password=Paas")
        .then(response => response.json())
        .then(data => _displayItems(data))
        .catch(error => console.error('Unable to get items.', error));
 
}

function setUserBusy() {
    const itemId = 6;
     

    fetch(uri+"/setuserbusy/"+itemId+"?id="+itemId, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        } 
    })
        .then(() => getItems())
        .catch(error => console.error('Unable to update item.', error));

    closeInput();

    return false;
}

function addSpecialItem() { 
    fetch(uri +"/PostSpecialists?userId=6&specialization=Pharmacist&languages=English,Yoruba,Idoma", {
        method: 'POST' 
    })
        .then(response => response.json())
        .then(() => {
            getItems();
            addNameTextbox.value = '';
        })
        .catch(error => console.error('Unable to add item.', error));
}

function addItem() { 
    $('#toast-1').toast('show');
    var addFirstName = document.getElementById('first-name').value.trim();
    var addLastName = document.getElementById('last-name').value.trim();
    var addLoginName = document.getElementById('user-name').value.trim();
    var addPassword = document.getElementById('password').value.trim();
    var addEmail = document.getElementById('email-address').value.trim();
    var addState = $("#state option:selected").text();
    var addLga = $("#lga option:selected").text();

    fetch(uri +"/PostUsers?firstname="+addFirstName+"&lastname="+addLastName+"&emailaddress="+addEmail+"&loginname="+addLoginName+"&password="+addPassword+"&stateoforigin="+addState+"&localgovt="+addLga, {
        method: 'POST' 
    })
        .then(response => response.json())
        .then(data => _processAddItems(data))
        .catch(error =>$('#snackbar-5').toast('show'));// console.error('Unable to add item.', error));
}

function deleteItem(id) {
    fetch(`${uri}/${id}`, {
        method: 'DELETE'
    })
        .then(() => getItems())
        .catch(error => console.error('Unable to delete item.', error));
}

function displayEditForm(id) {
    const item = todos.find(item => item.id === id);

    document.getElementById('edit-name').value = item.name;
    document.getElementById('edit-id').value = item.id;
    document.getElementById('edit-isComplete').checked = item.isComplete;
    document.getElementById('editForm').style.display = 'block';
}

function updateItem() {
    const itemId = document.getElementById('edit-id').value;
    const item = {
        id: parseInt(itemId, 10),
        isComplete: document.getElementById('edit-isComplete').checked,
        name: document.getElementById('edit-name').value.trim()
    };

    fetch(`${uri}/${itemId}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(item)
    })
        .then(() => getItems())
        .catch(error => console.error('Unable to update item.', error));

    closeInput();

    return false;
}

function closeInput() {
    document.getElementById('editForm').style.display = 'none';
}

function _displayCount(itemCount) {
    const name = (itemCount === 1) ? 'to-do' : 'to-dos';

    document.getElementById('counter').innerText = `${itemCount} ${name}`;
}

function _processAddItems(data) {

    var htm = "";
    data.forEach(item => {

         if(item.loginName.startsWith("Error")){
            $('#snackbar-5').toast('show');
         }

    });
    $('#toast-1').toast('hide');
}

function _displayItems(data) {
     
    console.log(data); 
    const button = document.createElement('button');
    var htm = "";
    $("#docItems").html(htm);
    data.forEach(item => {
         
        htm +="<div class='d-flex'>"
        htm += "            <div class='mr-3'>"
        htm += "                <img width='120' class='fluid-img rounded-m shadow-xl' src='images/pictures/31l.jpg'>"
        htm += "            </div>"
        htm += "            <div>"
        htm += "                <p class='color-highlight font-600 mb-n1'>"+item.firstName+" "+item.lastName +"</p>"
        htm += "                <h2>"+item.special+"</h2>"
        htm += "                <p class='color-green-dark font-600 mb-n1'>Can Communicate In:</p>"
        htm += "                <p class='mt-2'>"
        htm += "                    " + item.language +""
        htm += "                </p>"
        htm += "                <a href='index-components.html' class='btn btn-sm rounded-s font-13 font-600 gradient-highlight'><i class='fa fa-phone'></i> Call</a>"
        htm += "            </div>"
        htm += "        </div>"
            
        htm +="    <div class='divider mt-4'></div>"
         
    });
    $("#docItems").append(htm);
    todos = data;
}