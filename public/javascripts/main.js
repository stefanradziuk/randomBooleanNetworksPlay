window.onscroll = function () {
  const margin = 300;

  if ((window.innerHeight + window.pageYOffset + margin)
      >= document.body.offsetHeight) {

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.responseText.length > 0) {
        document.getElementById("rbn-tbody").innerHTML += this.responseText;
      }
    };

    const seed = document.getElementById("seed").value;
    const rowCount = document.getElementById("rbn-tbody").childElementCount

    xhttp.open("GET", `/load_more?seed=${seed}&count=${rowCount}`, false);
    xhttp.send();
  }
};