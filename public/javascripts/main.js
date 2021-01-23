let reachedEnd = false;

window.onscroll = function () {
  const margin = 300;

  if (!reachedEnd && (window.innerHeight + window.pageYOffset + margin)
      >= document.body.offsetHeight) {

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      document.getElementById("rbn-tbody").innerHTML += this.responseText;
      reachedEnd = this.responseText.includes("end");
    };

    const seed = document.getElementById("seed").value;
    const rowCount = document.getElementById("rbn-tbody").childElementCount

    xhttp.open("GET", `/load_more?seed=${seed}&count=${rowCount}`, false);
    xhttp.send();
  }
};