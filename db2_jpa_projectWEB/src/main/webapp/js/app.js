function addRows(opt, mon) {
	var table = document.createElement('table');
	var tr = document.createElement('tr');
	var array = ['Optional Name', 'Monthly Fee'];

	for (var j = 0; j < array.length; j++) {
		var th = document.createElement('th'); //column
		var text = document.createTextNode(array[j]); //cell
		th.appendChild(text);
		tr.appendChild(th);
	}
	table.appendChild(tr);

	for (var i = 0; i < array.length; i++) {
		var tr = document.createElement('tr');

		var td1 = document.createElement('td');
		var td2 = document.createElement('td');

		var text1 = document.createTextNode(opt);
		var text2 = document.createTextNode(mon);

		td1.appendChild(text1);
		td2.appendChild(text2);

		tr.appendChild(td1);
		tr.appendChild(td2);

		table.appendChild(tr);
	}
	document.body.appendChild(table);
}
