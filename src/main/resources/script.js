
document.addEventListener("DOMContentLoaded", function() {
    const navDataElements = document.querySelectorAll('.nav-data');
    const bottomNavDataElements = document.querySelectorAll('.bottom-nav-data');
    const mainList = document.getElementById('mainList');
    const bottomNavList = document.getElementById('bottomList');
    const parentMap = {};
    const headMap = {};

    // Create a map of parent elements
    navDataElements.forEach(element => {
        const id = element.id;
        const name = element.getAttribute('data-name');
        const order = parseInt(element.getAttribute('order'), 10);
        const type = element.getAttribute('data-type');
        const parentId = element.getAttribute('data-parent-id');

        const listItem = document.createElement('li');
        const anchor = document.createElement('a');
        anchor.href = `#${id}`;
        anchor.textContent = name;
        listItem.dataset.order = order;
        listItem.appendChild(anchor);

        if (type === 'child' && parentId) {
            if (!parentMap[parentId]) {
                parentMap[parentId] = {
                    element: null,
                    children: []
                };
            }
            parentMap[parentId].children.push(listItem);
        } else {
            if (!parentMap[id]) {
                parentMap[id] = {
                    element: listItem,
                    children: []
                };
            } else {
                parentMap[id].element = listItem;
            }
        }
    });

    // Create a map for bottom-nav-data elements organized by their data-head attribute
    bottomNavDataElements.forEach(element => {
        const id = element.getAttribute('data-id');
        const name = element.getAttribute('data-name');
        const order = parseInt(element.getAttribute('order'), 10);
        const head = element.getAttribute('data-head');

        const listItem = document.createElement('li');
        const anchor = document.createElement('a');
        anchor.href = `#${id}`;
        anchor.textContent = name;
        listItem.dataset.order = order;
        listItem.appendChild(anchor);

        if (!headMap[head]) {
            headMap[head] = [];
        }
        headMap[head].push(listItem);
    });

    // Function to sort elements based on their order attribute
    function sortElements(elements) {
        return elements.sort((a, b) => {
            return parseInt(a.dataset.order, 10) - parseInt(b.dataset.order, 10);
        });
    }

    // Append children to their respective parents, sorted by order
    Object.keys(parentMap).forEach(parentId => {
        const parentInfo = parentMap[parentId];
        const parentElement = parentInfo.element;

        if (parentElement) {
            if (parentInfo.children.length > 0) {
                const childList = document.createElement('ul');
                childList.classList.add('nested-list', 'hidden');
                const sortedChildren = sortElements(parentInfo.children);
                sortedChildren.forEach(child => {
                    childList.appendChild(child);
                });
                parentElement.appendChild(childList);
                parentElement.classList.add('has-children');

                parentElement.querySelector('a').addEventListener('click', function(event) {
                    event.preventDefault();
                    childList.classList.toggle('hidden');
                    window.location.href = this.href;
                });
            }
            mainList.appendChild(parentElement);
        }
    });

    // Sort and append top-level elements
    const topLevelElements = [];
    Object.keys(parentMap).forEach(parentId => {
        if (parentMap[parentId].element) {
            topLevelElements.push(parentMap[parentId].element);
        }
    });
    const sortedTopLevelElements = sortElements(topLevelElements);
    sortedTopLevelElements.forEach(element => {
        mainList.appendChild(element);
    });

    // Create and append head elements with their children for bottom-nav-data
    Object.keys(headMap).forEach(head => {
        const headItem = document.createElement('li');
        const headname = document.createElement('div');
        headname.textContent = head;
        headname.classList.add('nav-head');
        headItem.classList.add('has-children');
        headItem.id = head;

        const childList = document.createElement('ul');
        childList.classList.add('nested-list', 'hidden');
        const sortedHeadChildren = sortElements(headMap[head]);
        headname.textContent += ` (${sortedHeadChildren.length})`;
        sortedHeadChildren.forEach(child => {
            childList.appendChild(child);
        });

        headname.addEventListener('click', function() {
            childList.classList.toggle('hidden');
        });

        headItem.appendChild(childList);
        bottomNavList.appendChild(headItem);

        document.getElementById(head).insertAdjacentElement('beforebegin', headname);

    });
});

const parent = document.getElementById('navigation');

// Add click event listener to the parent
parent.addEventListener('click', (event) => {
    console.log(event.target.tagName)
    // Check if the clicked element is an <a> tag
    if (event.target && event.target.tagName === 'A') {
        // Remove 'selected' class from all <a> tags
        const links = parent.querySelectorAll('a');
        links.forEach(link => link.classList.remove('selected'));

        // Add 'selected' class to the clicked <a> tag
        event.target.classList.add('selected');
    }
});