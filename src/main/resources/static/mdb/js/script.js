// const search = () => {
//   let query = $('#search-input').val().trim();

//   if (query == '') {
//     $('#search-result').hide();
//   } else {
//     let url = `http://localhost:8080/search-post/${query}`;
//     fetch(url)
//       .then((response) => {
//         return response.json();
//       })
//       .then((data) => {
//         if (data.length === 0) {
//           $('#search-result').show();
//           let errorText = `<h3 class="text-center">No result Found</h3>`;
//           $('#search-result').html(errorText);
//         } else {
//           let text = `
//           <div class="col-lg-12 col-md-12 col-sm-12 justify-content-md-center">
//           <ul class="list-group my-2">`;
//           data.forEach((postQuery) => {
//             text += `
//          <a href="/post/${postQuery.id}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center
//           my-1 border-1 shadow"> ${postQuery.title}`;
//             text += `
//             <div>

//           <span class="badge badge-success ml-2">${postQuery.modifiedBy}</span>
//           </div>

//           </a>`;
//           });
//           text += `
//             </ul>
//             </div>`;

//           $('#search-result').html(text);
//           $('#search-result').show();
//         }
//       });
//   }
// };

$(document).ready(function () {
  $('#form-search').submit(function (e) {
    if (!$('#query-home').val().trim()) {
      e.preventDefault();
    }
  });
});
